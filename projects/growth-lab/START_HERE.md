# Growth Lab — two independent portfolio demonstrations

Built for Zethu Prudence Mkhize with AI assistance. Fresh code and fictional data; no FOREO material. Review, understand and adapt the implementation before presenting it as your work. These are portfolio demonstrations, not past client deployments.

## Run tonight
1. Install Python 3.10 or newer if needed (https://www.python.org/downloads/).
2. Extract the ZIP, then open a terminal in the `agent_studio` folder.
3. On Windows run `py server.py`. On Mac/Linux run `python3 server.py`.
4. Open http://127.0.0.1:8765 in your browser. Keep the terminal open.
5. Choose Lead qualification. Load the sample and run. Change the budget to zero and notice how the score and questions change.
6. Choose SEO brief, edit the topic and keywords, then generate and export JSON or Markdown.
7. Press Ctrl+C in the terminal to stop. No pip packages, API key or Docker is needed for demo mode.

Do not just double-click `static/index.html`; the Python backend must run.

## What is complete
- Local browser interface for two workflows, editable inputs and fictional samples.
- Explainable lead scoring with evidence, review queue and draft reply.
- Structured SEO brief with title, slug, meta description, outline and review checklist.
- JSON and Markdown exports; errors shown in the interface.
- Local Ollama connector with structured JSON validation and explicit error handling.
- Tests and example outputs.

## Demo mode versus AI mode
Demo mode uses transparent rules and templates. It does **not** call an LLM and must not be described as an autonomous AI agent. AI mode uses a local model for follow-up drafting and SEO brief generation. Scores and routing remain governed by explicit code. This is an AI-assisted workflow, not a self-directed tool-using agent. The application accepts automations, so that distinction is compatible with the brief.

## Connect a real local model
1. Install Ollama from https://ollama.com/download and start it.
2. Pick a model supported by your hardware from https://ollama.com/library. Download it with `ollama pull MODEL_NAME`, replacing MODEL_NAME with its exact model tag. Model size and memory requirements vary. Run `ollama list` to see installed tags.
3. Stop the Growth Lab server. Set the installed tag and restart:
   - Windows PowerShell: `$env:OLLAMA_MODEL="YOUR_INSTALLED_MODEL_TAG"` then `py server.py`
   - macOS/Linux: `OLLAMA_MODEL='YOUR_INSTALLED_MODEL_TAG' python3 server.py`
4. Select AI · local Ollama model and run a sample. Requests go from Python to `http://127.0.0.1:11434/api/chat`. No credentials are sent to this interface.
5. Review every result. Some models may return invalid JSON or fail length requirements; these are shown as errors rather than silently replaced with demo output.

Connector reference: https://docs.ollama.com/api/chat. It has been checked against the documented message, stream=false and JSON format fields. Live model inference was not executed in the build environment; test on your own installed model before recording the AI demonstration.

## Lead scoring policy (illustrative only)
Industry ecommerce/B2B/local services: 25. Service SEO/paid ads/Shopify/lead generation: 20. Monthly budget >=GBP 2,000: 25; GBP 750–1,999: 10. Timeline <=30 days: 15; <=90 days: 7. Identified decision maker: 15. Hot >=75; Warm >=45; otherwise Nurture. These thresholds are editable in core.py and are not a real agency's policy.

## Scope
No scraping, enrichment, live keyword research, messaging, publishing or CRM integration. SEO keywords are user supplied; search intent is a hypothesis. No traffic or revenue uplift has been measured. No email or LinkedIn account is connected. All actions stop at human review. No persistent database or authentication: keep the server local and do not expose it publicly. Model processing stays local if you use an actual local model rather than a cloud-backed model.

## Tests
From this folder: `python3 -m unittest discover -s tests -v` (Windows: use `py`). Tests cover score tiers, invalid data, structured output validation and the model path using mocked responses. Mock tests are not proof of live model performance.

## A three-minute Loom walkthrough
- 0:00–0:25: Introduce yourself and describe the agency problem: prioritising enquiries and producing consistent content briefs.
- 0:25–1:25: Load the fictional lead. Show the evidence and score, change one input, rerun and explain the routing. Show the human-review boundary.
- 1:25–2:20: Generate a brief and show headings, metadata, checks and export.
- 2:20–3:00: Explain Python validation, the local-model option, what you personally changed/tested, and the next integration you would build.

If you only ran demo mode, say so. Do not claim it was AI-generated. If you tested AI mode, show its mode badge and explain which parts the model generated. Do not claim production/client results. You can honestly describe observed sample outputs and validation results.

## First lead-generation idea
Start with qualification and routing for existing inbound enquiries. Define the ideal customer and scoring policy with the founder, test historical enquiries with permission, then add a reviewed CRM handoff. This prioritises existing demand without relying on invented lead data or automatically sending outreach.

## File map
core.py: validation, scoring, brief generation, local model connector.
server.py: local HTTP API and interface serving.
static/index.html: responsive UI and exports.
tests/test_core.py: automated checks.
examples/: reproducible fictional sample outputs.

## Reuse
Independent demonstration code may be adapted for your own portfolio. No third-party source code was copied. This package includes no proprietary project content. Keep claims about your authorship and measured outcomes accurate.
