## Como usar

#### 1- Salve os dois arquivos acima em um mesmo diretório

#### 2- Dê permissão de execução ao script de setup
```bash
  chmod +x setup.sh
```
### 3- Execute o script para iniciar o cluster:
```bash
  Shell ./setup.sh
```

### Para testar o failover, você pode parar o contêiner primário:
```bash
  docker stop mongo1
```
### E depois verificar que um dos secundários assumiu como primário:
```bash
  docker exec -it mongo2 mongosh --eval "rs.status()"
```