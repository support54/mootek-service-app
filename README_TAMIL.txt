MOOTEK SERVICE MANAGEMENT – FRESH ANDROID PROJECT

இந்த ZIP-ல் முழு Android Studio project மற்றும் GitHub Actions APK build workflow உள்ளது.

முக்கிய அம்சங்கள்:
1. Service Entry – தற்போதைய Service form, Dashboard, Records, Warranty, 3 நாள் Pending Alert, WhatsApp report, PDF/Print.
2. Call Entry – Customer call history, support method, status, follow-up, WhatsApp report.
3. R&D Entry – MRD ID, stage, findings, image/file attachments, WhatsApp report.
4. Android native SQLite database – Service/Call/R&D localStorage data SQLite app_store table-ல் சேமிக்கப்படும்.
5. Offline operation – app content local asset-ல் உள்ளது; internet இல்லாமலும் entries பயன்படுத்தலாம்.
6. Backup/Restore – HTML app-இல் உள்ள backup/restore வசதிகள்.
7. Java 17 + Kotlin JVM target 17 – இரண்டும் ஒரே target.
8. GitHub Actions workflow: .github/workflows/build-apk.yml

GITHUB MOBILE UPLOAD STEPS:
1. GitHub repository-க்கு செல்லவும்.
2. Code tab > Add file > Upload files.
3. இந்த ZIP-ஐ mobile-ல் Extract செய்யவும்.
4. ZIP உள்ளே இருக்கும் எல்லா files/folders-ஐ repository root-க்கு upload செய்யவும். .github folder-ஐயும் சேர்க்க வேண்டும்.
5. Commit changes.
6. Actions tab > Build MOOTEK Android APK > Run workflow.
7. Build success ஆனதும் run page-ல் Artifacts பகுதியில் MOOTEK-Android-APK download செய்யவும்.

குறிப்பு:
- இந்த project source package. இங்கு APK compile செய்து வெற்றி என்று கூறவில்லை; GitHub Actions-ல் build செய்ய வேண்டும்.
- App data Android app-ன் private SQLite database-ல் இருக்கும். App uninstall செய்தால் Android app data அழியலாம். அதனால் அவ்வப்போது JSON backup export செய்யவும்.
- Existing Service Entry UI/fields மற்றும் Call/R&D modules source HTML-ல் உள்ளபடி வைக்கப்பட்டுள்ளன.
