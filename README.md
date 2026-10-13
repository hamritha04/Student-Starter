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



\## Branch

Feature branch: `feature/course-registration-set-a`

