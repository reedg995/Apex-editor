# APEX Editor — Android starter (v0.1)

A native Android Studio starter for a karting-focused video editor.

## Included in this first build
- Dark, black-and-green APEX interface
- Android video picker (imports a clip using the system document picker)
- Basic in-app video preview
- Karting edit-style presets: NFS Heat, F1 Cinematic, Raw Race, Hype Cut
- User-editable preferences for highlight focus, beat sync, slow motion and output aspect ratio

## Important: what is not built yet
The "BUILD MY EDIT" button currently saves the selected preferences in the UI and reports the next development step. It does **not** run AI analysis, cut footage, add effects, or export a rendered video yet. Those require a real video-processing pipeline and an AI highlight-analysis component.

## Open it
1. Install Android Studio.
2. Choose **Open** and select this `APEXEditor` folder.
3. Let Gradle sync.
4. Connect an Android phone with USB debugging enabled, or use an emulator.
5. Press Run.

## Next engineering stages
1. Add a timeline with multiple clips and trim handles.
2. Add video metadata analysis and scene/motion/audio event detection.
3. Implement actual trimming, transitions, speed ramps, colour grading and music mixing using a rendering engine.
4. Add export presets for 9:16 and 16:9.
5. Add an optional cloud AI service for semantic highlight selection. Keep the user's footage private and require explicit upload consent.

## Camera note
Use camera mounts only where the karting venue permits them. Do not attach a camera to a helmet or kart in a way that could compromise safety.
