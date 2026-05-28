
# Comment le Backend Java Traite une demande de l'utilisateur ? 

La base de données et S3 ne se parlent pas directement 
Frontend
   ↓
Backend Java (FileService)
    ↓              ↓
PostgreSQL       AWS S3
(métadonnées)    (fichier physique)


## PostgreSQL stocke les métadonnées :

nom du fichier
taille
token
date d'expiration
storagePath (le nom unique sur S3)

## AWS S3 stocke le fichier physique réel (le PDF, l'image, etc.)




# Le flux complet pour envoyer un fichier. 

Frontend
   ↓ envoie FileDTO (nom, taille, date)
FileController
   ↓ passe à
FileService
   ↓ sauvegarde sur S3 + en base
   ↓ construit la réponse
FileResponseDTO
   ↓ renvoie au
Frontend (token, nom, taille, dates)



# Le rôle du controller

le File Controller est une étape en fait de contrôle avant de passer les infos au FileService
Le FileController est la porte d'entrée de l'application

Frontend
   ↓
FileController  ← "Est-ce que la requête est valide ?"
   ↓
FileService     ← "Je traite la logique métier"
   ↓
FileRepository  ← "Je sauvegarde en base"
+ StorageService ← "Je sauvegarde sur S3"
   ↓
FileResponseDTO ← "Je renvoie la réponse"
   ↓
Frontend


