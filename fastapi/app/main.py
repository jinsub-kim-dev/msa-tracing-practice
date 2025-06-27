# fastapi/app/main.py
from fastapi import FastAPI
import time, random
from fastapi.responses import JSONResponse

from opentelemetry import trace
from opentelemetry.sdk.resources import SERVICE_NAME, Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.exporter.zipkin.json import ZipkinExporter
from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
from opentelemetry.instrumentation.requests import RequestsInstrumentor


app = FastAPI()

# OpenTelemetry 설정
trace.set_tracer_provider(
    TracerProvider(
        resource=Resource.create({SERVICE_NAME: "fastapi-service"})
    )
)
zipkin_exporter = ZipkinExporter(
    endpoint="http://zipkin:9411/api/v2/spans"
)
trace.get_tracer_provider().add_span_processor(
    BatchSpanProcessor(zipkin_exporter)
)

# FastAPI + 요청 계측
FastAPIInstrumentor.instrument_app(app)
RequestsInstrumentor().instrument()


@app.get("/api/profile/{user_id}")
def get_profile(user_id: str):
    delay = random.uniform(1, 3)
    time.sleep(delay)
    return JSONResponse(content={
        "userId": user_id,
        "nickname": f"user_{user_id}",
        "bio": "This is a mocked user profile.",
        "delay": f"{delay:.2f}s"
    })
