# Simple HTTP server for benchmark metrics
FROM python:3.11-alpine

WORKDIR /app

# Simple HTTP server script
RUN echo 'import http.server' > server.py && \
    echo 'import socketserver' >> server.py && \
    echo 'import os' >> server.py && \
    echo '' >> server.py && \
    echo 'PORT = 9100' >> server.py && \
    echo '' >> server.py && \
    echo 'class Handler(http.server.SimpleHTTPRequestHandler):' >> server.py && \
    echo '    def do_GET(self):' >> server.py && \
    echo '        if self.path == "/metrics":' >> server.py && \
    echo '            self.send_response(200)' >> server.py && \
    echo '            self.send_header("Content-type", "text/plain; version=0.0.4")' >> server.py && \
    echo '            self.end_headers()' >> server.py && \
    echo '            try:' >> server.py && \
    echo '                with open("/benchmarks/metrics.prom", "rb") as f:' >> server.py && \
    echo '                    self.wfile.write(f.read())' >> server.py && \
    echo '            except FileNotFoundError:' >> server.py && \
    echo '                self.wfile.write(b"# No metrics available yet")' >> server.py && \
    echo '        else:' >> server.py && \
    echo '            self.send_response(200)' >> server.py && \
    echo '            self.send_header("Content-type", "text/plain")' >> server.py && \
    echo '            self.end_headers()' >> server.py && \
    echo '            self.wfile.write(b"Benchmark Metrics Server\\n/metrics - Prometheus metrics")' >> server.py && \
    echo '' >> server.py && \
    echo 'with socketserver.TCPServer(("", PORT), Handler) as httpd:' >> server.py && \
    echo '    print(f"Server running on port {PORT}")' >> server.py && \
    echo '    httpd.serve_forever()' >> server.py

EXPOSE 9100

CMD ["python", "server.py"]

