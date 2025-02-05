FROM python:3.9-slim

RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    python3-dev \
    && rm -rf /var/lib/apt/lists/*

RUN pip3 install --no-cache-dir --upgrade pip

COPY requirements.txt /tmp/

RUN pip3 install --no-cache-dir -r /tmp/requirements.txt \
    && rm -rf /root/.cache

RUN mkdir -p /opt/ml/code

COPY inference.py /opt/ml/code/
COPY run_ga.py /opt/ml/code/

WORKDIR /opt/ml/code

EXPOSE 8080

CMD ["python3", "/opt/ml/code/inference.py"]

