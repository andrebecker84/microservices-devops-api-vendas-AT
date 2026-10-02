# Como rodar no Kubernetes

## 1. Instalar (só uma vez)

```bash
brew install kubectl kind
```

## 2. Subir o cluster

```bash
kind create cluster --name ecommerce
```

## 3. Buildar e carregar as imagens

```bash
for s in eureka-server config-server produtos-service vendas-service clientes-service fornecedores-service gateway; do
  docker build -t $s:1.0 ./$s
  kind load docker-image $s:1.0 --name ecommerce
done
```

## 4. Aplicar os manifests

```bash
kubectl apply -f k8s/
```

## 5. Ver se subiu

```bash
kubectl get pods -n ecommerce -w
```

Espera até todos ficarem `1/1 Running` (Ctrl+C pra sair do watch).

## 6. Acessar

Os Services sao ClusterIP (so existem dentro do cluster). Pra acessar do Mac,
abre um tunel:

```bash
kubectl port-forward -n ecommerce svc/gateway 8085:8085
```

Depois, tudo passa pelo gateway usando o nome do servico como prefixo:

```bash
curl http://localhost:8085/produtos-service/produtos
curl http://localhost:8085/vendas-service/vendas
```

Pra ver o painel do Eureka: `kubectl port-forward -n ecommerce svc/eureka-server 8761:8761`

## Tres pegadinhas que quebram o acesso

Se os pods ficarem `0/1 Running` pra sempre, ou o gateway devolver 404/500,
provavelmente e um destes:

**1. config-server com backend git apontando pro ConfigMap.** O ConfigMap monta
os `.properties` como arquivos soltos, sem `.git`, e o backend git morre com
`No .git at file:///config-repo`. No k8s use o backend **native**
(`SPRING_PROFILES_ACTIVE=native` + `SPRING_CLOUD_CONFIG_SERVER_NATIVE_SEARCH_LOCATIONS`).
Como o `spring.config.import` e `optional:`, a app sobe mesmo assim — so que sem
o `server.port`, indo pra 8080 padrao, e o readinessProbe em 8081/8082 nunca passa.

**2. `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` nao funciona sozinho.** `serviceUrl` e
um `Map`, e a env var vira a chave minuscula `defaultzone`; o Eureka le `defaultZone`,
entao o default `localhost:8761` vence e o servico nunca se registra. Use
`SPRING_APPLICATION_JSON`, que preserva o case.

**3. Eureka registrando o hostname do Pod.** Pod nao tem registro no DNS do cluster,
so Service — o gateway falha com `UnknownHostException` / `NXDOMAIN`. Force o IP do
Pod com `EUREKA_INSTANCE_PREFER_IP_ADDRESS` + `EUREKA_INSTANCE_IP_ADDRESS` vindo da
downward API (`status.podIP`).

Comandos uteis pra diagnosticar:

```bash
kubectl get pods -n ecommerce                                    # quem esta 0/1
kubectl logs -n ecommerce deploy/produtos-service | grep Tomcat  # subiu em qual porta?
kubectl exec -n ecommerce deploy/config-server -- \
  wget -qO- http://localhost:8888/produtos-service/docker        # o config chega?
kubectl exec -n ecommerce deploy/eureka-server -- \
  wget -qO- http://localhost:8761/eureka/apps | grep homePageUrl # registrou por IP?
```

---

### Mudei o código de um serviço, e agora?

```bash
docker build -t produtos-service:1.0 ./produtos-service
kind load docker-image produtos-service:1.0 --name ecommerce
kubectl rollout restart deployment/produtos-service -n ecommerce
```

### Pra derrubar tudo

```bash
kind delete cluster --name ecommerce
```


