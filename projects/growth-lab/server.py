"""Local-only server using Python standard library. No third-party dependencies."""
import json, os
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.parse import urlparse
from core import run, SAMPLE_LEAD, SAMPLE_SEO
ROOT=Path(__file__).parent
class Handler(BaseHTTPRequestHandler):
    def send(self,status,value,ctype='application/json'):
        body=json.dumps(value).encode() if ctype=='application/json' else value
        self.send_response(status); self.send_header('Content-Type',ctype); self.send_header('Content-Length',str(len(body))); self.send_header('X-Content-Type-Options','nosniff'); self.send_header('Cache-Control','no-store'); self.end_headers(); self.wfile.write(body)
    def do_GET(self):
        p=urlparse(self.path).path
        if p=='/api/samples': return self.send(200,{'lead':SAMPLE_LEAD,'seo':SAMPLE_SEO,'model_configured':bool(os.getenv('OLLAMA_MODEL'))})
        if p in ['/','/index.html']: return self.send(200,(ROOT/'static/index.html').read_bytes(),'text/html; charset=utf-8')
        return self.send(404,{'error':'Not found'})
    def do_POST(self):
        # Reject cross-site browser requests. Model runs are localhost-only.
        origin=self.headers.get('Origin')
        if origin and origin not in ['http://127.0.0.1:8765','http://localhost:8765']: return self.send(403,{'error':'Cross-origin request rejected'})
        if urlparse(self.path).path!='/api/run': return self.send(404,{'error':'Not found'})
        try:
            length=int(self.headers.get('Content-Length','0'))
            if not 0<length<=100000: raise ValueError('Request must be 1-100000 bytes.')
            raw=json.loads(self.rfile.read(length)); result=run(raw.get('kind'),raw.get('data'),raw.get('mode','demo'))
            return self.send(200,result)
        except (ValueError,TypeError,AttributeError) as exc: return self.send(400,{'error':str(exc)})
        except Exception: return self.send(500,{'error':'Unexpected processing error. Check local server logs.'})
    def log_message(self,*args): pass
if __name__=='__main__':
    print('Open http://127.0.0.1:8765 - Ctrl+C to stop. Local-only; do not expose publicly.')
    ThreadingHTTPServer(('127.0.0.1',8765),Handler).serve_forever()
