gsan-operacional
================

No Gsan Operacional, é possível visualizar o comportamento dos componentes que mantem as atividades da Cosanpa.

Basicamente, o Gsan Operacional se utiliza de cadastros dos seguintes elementos para emitir relatórios e indicadores:

* Unidades consumidoras de energia
* Contratos realizados com a companhia distribuidora de energia
* Macromedidores com os tipos de medição por vazão, pressão e nível 
* Estações operacionais de água bruta, água tratada e de tratamento de água
* Produtos químicos: quais são, preços e consumos

**Como indicadores temos:**

* prazos de atendimentos de ordens de serviço
* eficiências de retiradas de vazamentos
* conformidade da água
* tratamento de esgoto
* clientes com precariedade no abastecimento de água


**Tecnologias utilizadas**

* Java EE 7
* Java 8
* Servidor de aplicação: WildFly 8.0.0
* Banco de dados: Postgres 9.3.4

**Configuração da aplicação Java:**

* Instale o driver do postgres no Wildfly (pode usar a lib armazenada na pasta migracoes/drivers do projeto gsan-persistence)
* Crie um datasource no arquivo standalone-full.xml com o jndi 'java:jboss/datasources/GsanDS' (o mesmo do persistence.xml)
* Execute a aplicação com a versão full do wildfly (standalone-full.xml)

**Instalação do Apache Archiva**

[Apache Archiva](https://github.com/prodigasistemas/gsan/wiki/Instala%C3%A7%C3%A3o-do-Archiva)

**Configuraçao do Apache Archiva (Opcional):**

Crie ou altere o arquivo settings.xml na pasta .m2 do Maven:

    <servers>   
      <server>
      <id>archiva.internal</id>
      <username>:usuario</username>
      <password>:senha</password>
     </server>
    </servers>  
    <profiles>   
      <profile>
        <id>Repository Proxy</id>
        <activation>
          <activeByDefault>true</activeByDefault>
        </activation>
        <!-- place repo xml here -->
    <repositories>
     <repository>
       <id>internal</id>
       <name>Archiva Managed Internal Repository</name>
       <url>http://archiva.[nome_repositorio]/repository/internal/</url>
      <releases>
       <enabled>true</enabled>
      </releases>
      <snapshots>
       <enabled>true</enabled>
      </snapshots>
     </repository>
    </repositories>
    <pluginRepositories>
       <pluginRepository>
         <id>internal</id>
           <name>Archiva Managed Internal Repository</name>
           <url>http://archiva.[nome_repositorio]/repository/internal/</url>
         <releases>
           <enabled>true</enabled>
         </releases>
          <snapshots>
           <enabled>true</enabled>
         </snapshots>
       </pluginRepository>
      </pluginRepositories>
     </profile>
    </profiles>   



