# AudioRecorderService

Es un servicio que permite la grabación de audio en segundo plano. Para ello se utilizaron dos librerías de Java: 
- MediaRecorder: Para grabar audios y codificarlos.
- AlarmManager: Permite que el servicio se ejecute cada cierto tiempo.

Además cuenta con un menú que permite configurar varias características de audio. Contiene las siguientes opciones:
- Formato del archivo de audio: Puede ser grabado en m4a o 3gp.
- Duración del audio: Tiempo de grabación de un audio.
- Tiempo de espera para siguiente grabación: Representa cada cuanto tiempo debe grabar.
- Número de canales de audio: Tiene 2 opciones, mono o stereo. 
- Tasa de bits (Bitrate): Se encuentra en Kbps. Puede ser de 16 kbps hasta 448 kbps. Si se está grabando en mono solo se puede hasta 256 kbps.
- Número de muestras: Se encuentra en Khz. Puede ser 48 kHz, 44.1 kHz o 16 kHz. 

El servicio puede ser ejecutado en celulares con versiones de Android mayores o iguales a 2.3.3 (API level >= 10). 
