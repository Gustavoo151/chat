#!/bin/bash

# Iniciar os containers
docker-compose up -d

# Aguardar tempo suficiente para inicialização
echo "Aguardando inicialização dos containers MongoDB..."
sleep 20

# Verificar status do replica set
echo "Verificando status do replica set:"
docker exec mongo1 mongosh --eval "rs.status()"

echo "Configuração do replica set MongoDB concluída!"
echo "Você pode conectar ao MongoDB utilizando a string de conexão: mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=rs0"