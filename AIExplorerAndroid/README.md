
# AI Explorer – Android (WebView Wrapper)

Open in **Android Studio**, set your web URL in `app/build.gradle.kts` (APP_URL), and run.

- Debug default: `http://10.0.2.2:8501` (Streamlit on host) — change to `:3000` if pointing to Next.js.
- Release default: `https://your-streamlit-or-nextjs-url`

Notes:
- Google OAuth blocks WebView sign-ins. The app opens such links in **Chrome Custom Tabs**. For full auth, prefer a PWA flow or implement token handoff to `aiexplorer://callback`.
- Pull-to-refresh, progress bar, external link handling included.
