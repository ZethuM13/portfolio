# Growth Lab

Two Python and Ollama workflows: lead qualification and SEO content briefs. Built with AI assistance; independently tested on Windows with Qwen2.5 3B.

## Run
From this directory, run `py server.py` (Windows) or `python3 server.py`. Open http://127.0.0.1:8765.

For local AI in Windows Command Prompt, first run `set "OLLAMA_MODEL=qwen2.5:3b"`, then `py server.py`. Install and run Ollama and download that model first.

Run tests: `py -m unittest discover -s tests -v`.

See START_HERE.md for details. The examples directory contains rules/template outputs, not live AI output. No messaging, publishing or production claims. Human review required.
