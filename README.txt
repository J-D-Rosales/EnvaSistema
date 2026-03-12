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

