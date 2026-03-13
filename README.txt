# Endpints necesarios para el proyecto (mangas):
- POST URL/nuevo
Diseñado para colocar nuevas mangas a la base de datos mediante el escaneo de qr.
Input(Dado por la aplicación):
{
    codigo_qr: String,
    tipo_operacion: String,
    locacion_origen: String,
    locacion_destino: String,
    operario_id: String,
    metadatos: String,
    timestamp: String,
    isSynced: Boolean = false // this is just for the app not for the database
}
- POST URL/salida
Diseñado para sacar mangas de la base de datos mediante el escaneo de qr.
Input(Dado por la aplicación):
{
    codigo_qr: String,
    tipo_operacion: String,
    locacion_origen: String,
    locacion_destino: String,
    operario_id: String,
    metadatos: String,
    timestamp: String,
    isSynced: Boolean = false // this is just for the app not for the database
}

- PUT URL/movimiento (Recordar 2 escaneos) uno desde el sitio a enviar y otro desde el enviado.
Input(Dado por la aplicación):
{
    codigo_qr: String,
    tipo_operacion: String,
    locacion_origen: String,
    locacion_destino: String,
    operario_id: String,
    metadatos: String,
    timestamp: String,
    isSynced: Boolean = false // this is just for the app not for the database
}

# What the applicaction can do until now.
- He cna save the data located in offline
- Can upload the changes and errase when it is sync.
- It can scan and use the camera.
- It can scan several times.
# Features required,
Whenever it scan it shows the user as cards The car is acomponent
in compononets it calls from the information of the qr
- Knowing the things of the payload and the time of cousr.  (ALso para turno mañana)
- add three atributes for the sending of the payload.
- Login and register with the functionality of recolecting the site of the user is right now
  The user can change where his site is.
All your work.

Today:
Design of the card. And the card in code.  And the realtionship of the attributes of the card. (DUE TODAY)
CARD: NOMBRE PESO Y HORA.,

SHOW THAT THE DATABASE IS WORKING FINE (DUE TODAY)
Due the other day: (Add the three attributes for sending the payload). It's nmot working the datbase yet
Showing time, depending on the time of the computer/phon/chaineay (Due other day. but don today.)
Login y register al último papa.

10452,M-500,Extrusora 3,OP-2026-99,Mañana,2026-03-12,5,Juan Perez,Azul,2026-03-12T10:30:00,25.5
10453,M-500,Extrusora 3,OP-2026-99,Mañana,2026-03-12,5,Juan Perez,Azul,2026-03-12T10:30:00,25.5