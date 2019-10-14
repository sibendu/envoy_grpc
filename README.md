# This is a sample project for using envoy with gRPC services

Steps to Run:
Create ConfigMap
$ kubectl create configmap sidecar-config --from-file=front_envoy/envoy-config-front.yaml --from-file=service_a/envoy-config-a.yaml --from-file=service_b/envoy-config-b.yaml --from-file=service_c/envoy-config-c.yaml

Deploy Services
$ kubectl create -f servicec.yaml

$ kubectl create -f serviceb.yaml

$ kubectl create -f servicea.yaml

$ kubectl create -f frontenvoy.yaml

Setup envoy monitoring
Deploy statsd-exporter for Prometheus $ kubectl create -f statsd.yaml

This will created statsd-exporter on 9125 (Statsd input) and 9102 (Prometheus output); will create a LoadBalancer service

Install prometheus and grafana

Add prometheus scrape target for statsd-exporter : localdbalancer-ip:9102 (N.B. on OKE, there might be limit on LoadBalancer on the account)

Import grafana_EnvoyServices.json (This is generated through grafanalib using ./grafana/service-dashboard.py))

Test:
$ kubectl port-forward {forntenvoy-pod} 8080:80 Now you can http://localhost:8080

To generate traffic - curl -s "http://localhost:8080/[1-1000]"
