\# Course Registration System



\## Overview

A Java 17 and Maven project implementing course registration with credit-limit, course-capacity, prerequisite, active-course, and duplicate-registration validation.



\## Application Changes

\- Maximum permitted credits: 24

\- Reject registration when a course has reached capacity

\- Validate prerequisites and course activity

\- Prevent duplicate registrations

\- Include boundary tests for credit limits and course capacity



\## Project Structure

\- `pom.xml` — Maven parent project

\- `course-registration-app/` — Java application and tests

\- `Jenkinsfile` — Jenkins CI pipeline

\- `deployment.yaml` — Kubernetes Nginx deployment

\- `service.yaml` — Kubernetes NodePort service



\## Build and Test

Run from the repository root:



```bash

mvn clean test

mvn package

```



The application tests previously completed successfully: 12 tests, 0 failures, 0 errors, and 0 skipped.



\## Jenkins Pipeline

The pipeline checks out the repository, runs Maven tests, packages the application, publishes JUnit test reports, and archives the generated JAR.



\## Kubernetes Deployment

Deploy Nginx to Minikube:



```bash

kubectl apply -f deployment.yaml

kubectl apply -f service.yaml

kubectl get deployments

kubectl get pods

kubectl get services

minikube service nginx-service --url

```



The deployment was verified with two ready Nginx replicas and a NodePort service on port `30080`.

## Kubernetes Scaling and Self-Healing Evidence

The Nginx application was deployed on the local Minikube cluster using the Deployment and NodePort Service manifests.

### Scale the Deployment

Command:

```bash
kubectl scale deployment nginx-deployment --replicas=3
kubectl rollout status deployment/nginx-deployment
kubectl get deployments
kubectl get pods
```

Observed result:
- Rollout completed successfully.
- Deployment showed 3/3 ready replicas.
- Three Nginx pods were running.

### Demonstrate Self-Healing

A running Nginx pod was deleted:

```bash
kubectl delete pod nginx-deployment-75cc477c85-cj5f6
kubectl get deployments
kubectl get pods
```

Observed result:
- The selected pod was deleted successfully.
- Kubernetes created a replacement pod named `nginx-deployment-75cc477c85-pd6s6`.
- The Deployment returned to 3/3 ready replicas, with three running pods.

### Explanation

The Deployment controller continuously compares the actual number of replicas with the desired replica count. When a pod is deleted, it creates a replacement to restore the desired state.

### Evidence

Screenshots and command outputs were captured from the actual Minikube execution. They demonstrate successful scaling and pod self-healing.


\## Branch

Feature branch: `feature/course-registration-set-a`

