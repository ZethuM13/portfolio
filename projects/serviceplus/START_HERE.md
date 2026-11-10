# Start Here on Windows

This milestone is a working preview of ServicePlus. You can open the code in Visual Studio Code and run the application in your web browser.

## 1. Extract the ZIP file

1. Download `ServicePlus_Milestone_1.zip`.
2. Right-click the ZIP file and select **Extract All**.
3. Open the extracted `ServicePlus_Milestone_1` folder.

Do not try to run the application from inside the compressed ZIP file.

## 2. Open the project in Visual Studio Code

1. Open Visual Studio Code.
2. Select **File > Open Folder**.
3. Select the extracted ServicePlus folder.
4. If Visual Studio Code asks whether you trust the authors, select **Yes, I trust the authors** because this is our project.

You should now see these folders in the Explorer panel:

- `backend` - the Java API and service data.
- `frontend` - the page, styling and browser behaviour.
- `docs` - the requirements and architecture.
- `scripts` - the commands used to run and test the application.

## 3. Confirm Java is installed

1. In Visual Studio Code, select **Terminal > New Terminal**.
2. Enter:

```powershell
java -version
```

ServicePlus requires Java 17 or newer. If Windows says that `java` is not recognised, stop and tell Codex exactly what appears in the terminal.

## 4. Start ServicePlus

In the Visual Studio Code terminal, enter:

```powershell
scripts\START_SERVICEPLUS.bat
```

A new terminal window will open for the ServicePlus server. Your browser should then open automatically at:

```text
http://localhost:8080
```

Keep the server window open while using the application. Close that window when you want to stop ServicePlus.

## 5. Explore the first milestone

On the dashboard you can:

- View the registered-service totals.
- See which services are healthy, degraded or unavailable.
- Search by service name, owner or business domain.
- Select **Register service** and add a new catalogue entry.

The data is currently stored in memory. It resets to the four demonstration services whenever the server is stopped and restarted. Persistent PostgreSQL storage will be added in a later milestone.

## Files to open first

1. `frontend/index.html` - the structure of the dashboard.
2. `frontend/styles.css` - the colours, spacing and responsive layout.
3. `frontend/app.js` - loading, searching and registering services.
4. `backend/src/com/serviceplus/Main.java` - the API and temporary data store.

The Linux and macOS `run.sh` file can be ignored on Windows.
