# AUDIO RECORDER SERVICE
It is a service that allows audio recording in the background.<br/>
![Diagrama de interacción de clases](https://github.com/rfcx-espol/AudioRecorderService/blob/master/diagrama.jpg?raw=true)

## Features
It has a menu that allows you to configure the following audio options:
- Audio format: m4a or 3gp.
- Audio duration: Recording time of an audio.
- Waiting time for next recording: How often you should record
- Number of audio channels: mono or stereo.
- Bitrate: From 16 kbps to 448 kbps. If you are recording in mono, you can only configure to 256 kbps.
- Number of samples: 48 khz, 44.1 khz or 16khz.

## System requirements
- Minimum Android API: 10 <br/>
- Android version: 2.3.5

## Directories
- java directory:

  - Activities: <br/>
    **- MainActivity:** Starts the service for the first time. <br/>
    **- PrefsActivity:** Starts the audio preferences menu. Changes made by the user. <br/>
  - Services: <br/>
    **- ReceiverCall:** Starts the service after the device's restart. <br/>
  - Services: <br/>
    **- AudioRecorderService:** Start recording everything that is close to the device. <br/>
  - Utils: <br/>
    **- FileUtils:** Find or create the directory file when audio will be saved. <br/>
    **- Identifiers:** Contains the global variables and static variables. <br/>
- res directory (layouts):

  - layout: <br/>
    **- activity_main:** Blank layout to start the app. <br/>
  - menu: <br/>
    **- main_menu:** Contains the audio's preferences options. <br/>
  - xml: <br/>
    **- prefs:** Allows to see and modify the preferences. <br/><br/>

## Libraries
- MediaRecorder: Record audios and allows you to configure it <br/>
- AlarmManager: Activate the service every so often
