#!bin/bash

CURRENT_PATH=$(pwd)

# Constroi o build
cd $OPERACIONAL_PATH
mvn clean package -Dmaven.test.skip=true

# Compacta o build
zip -vr target/gsan-operacional.war.zip target/*.war

# Transfere o build para o servidor
echo "Porta SSH (22):"
if [ -z $PORTA ]; then
  read porta
  if [ -z $porta ]; then
    porta=22
  fi
else
  porta=$PORTA
fi

echo "Usuario Remoto ($USER):"
if [ -z $USUARIO ]; then
  read usuario
  if [ -z $usuario ]; then
    usuario=$USER
  fi
else
  usuario=$USUARIO
fi

echo "IP Remoto (127.0.0.1):"
if [ -z $IP_REMOTO ]; then
  read ip_remoto
  if [ -z $ip_remoto ]; then
    ip_remoto=127.0.0.1
  fi
else
  ip_remoto=$IP_REMOTO
fi

echo "Caminho Remoto (/tmp):"
if [ -z $CAMINHO_REMOTO ]; then
  read caminho_remoto
  if [ -z $caminho_remoto ]; then
    caminho_remoto=/
  fi
else
  caminho_remoto=$CAMINHO_REMOTO
fi

scp -P $porta $OPERACIONAL_PATH/target/gsan-operacional.war.zip $usuario@$ip_remoto:$caminho_remoto

# Apaga o build transferido e volta ao local inicial
rm -rf $OPERACIONAL_PATH/target/gsan-operacional.war.zip

cd $CURRENT_PATH
