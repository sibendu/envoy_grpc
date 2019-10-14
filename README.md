# Sample project - using envoy with gRPC services

Steps to Run:

1. Create ConfigMap
$ kubectl create configmap sidecar-config --from-file=front_envoy/envoy-config-front.yaml --from-file=service_a/envoy-config-a.yaml --from-file=service_b/envoy-config-b.yaml --from-file=service_c/envoy-config-c.yaml

2. Deploy Services
$ kubectl create -f servicec.yaml

$ kubectl create -f serviceb.yaml

$ kubectl create -f servicea.yaml

$ kubectl create -f frontenvoy.yaml


