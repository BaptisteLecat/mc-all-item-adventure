FROM itzg/minecraft-server

ENV TYPE=PAPER

# Définir un dossier temporaire pour stocker tous les plugins
ARG PLUGINS_DIR=/plugins_temp

# Copier tous les fichiers .jar dans le répertoire des plugins de Minecraft
COPY ${PLUGINS_DIR}/*.jar /plugins/
