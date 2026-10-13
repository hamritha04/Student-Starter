# Course Registration System

## 1. Overview

A Java 17 and Maven application for course registration. This project fixes the course-capacity boundary defect and implements the Question Set A requirement to increase the maximum permitted registered credits from 20 to 24.

## 2. Application Changes

- Increased the maximum permitted total credits to 24.
- Rejected registrations when a course is already at capacity.
- Allowed registration when exactly one seat remains.
- Preserved student registration and credit state, and course enrollment count, after rejected attempts.
- Retained validation for prerequisites, active courses, and duplicate registrations.
- Added focused JUnit 5 tests for credit and capacity boundaries.

## 3. Project Structure

```text
Student-Starter/
├── pom.xml
├── course-registration-app/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/university/
│       └── test/java/com/university/
├── Jenkinsfile
├── deployment.yaml
├── service.yaml
└── README.md
```

## 4. Maven Configuration and Build Evidence

The root `pom.xml` is the parent POM with `pom` packaging. It defines shared properties, including Java 17 and the JUnit version, manages the JUnit 5 dependency, and declares the `course-registration-app` module.

The child POM inherits the parent coordinates using `../pom.xml`, declares the JUnit dependency without repeating its version, and configures the compiler and Surefire plugins for compilation and JUnit 5 testing.

Run these commands from the repository root:

```bash
mvn clean test
mvn package
```

Verified results:

- Tests run: 12
- Failures: 0
- Errors: 0
- Skipped: 0
- Maven reactor: parent and child modules succeeded
- Packaging: successful

The generated JAR is located at:

`course-registration-app/target/course-registration-app-1.0-SNAPSHOT.jar`

## 5. Git Workflow

Feature branch: `feature/course-registration-set-a`

Relevant commit history:

- `cef2bff` — Initial student assessment starter
- `8fadb3d` — Fix capacity validation and update credit limit
- `16be755` — Fix registration limits and add boundary tests
- `a045ef3` — Add Jenkins pipeline and Kubernetes deployment
- `7c43072` — Merge course registration and DevOps implementation
- `1075be6` — Configure Maven for Jenkins pipeline
- `ed54709` — Document Kubernetes scaling and self-healing

The feature branch was merged into `main`, and the work was pushed to GitHub.

## 6. Jenkins Declarative Pipeline

The root `Jenkinsfile` targets a Windows Jenkins agent and uses `bat` commands.

The pipeline performs these stages:

1. Checkout — retrieves the repository.
2. Build and Test — runs `mvn -B clean test`.
3. Package — runs `mvn -B package`.
4. Archive Artifact — archives the generated JAR.

The Maven commands run from the repository root.

JUnit report path:

`course-registration-app/target/surefire-reports/TEST-*.xml`

Artifact path:

`course-registration-app/target/*.jar`

The pipeline publishes test reports in the post-test action. If the test command fails, Declarative Pipeline skips the subsequent Package and Archive Artifact stages. The JUnit publisher is configured with `allowEmptyResults: true` so missing reports do not obscure the original test failure.

A Jenkins execution was performed. The pipeline stages completed successfully, and the test results and archived artifact were verified.

## 7. Kubernetes Deployment on Minikube

The manifests deploy the `nginx:1.27` image with two replicas and expose it using a NodePort Service on port `30080`.

Apply the manifests:

```bash
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl get deployments
kubectl get pods
kubectl get services
minikube service nginx-service --url
```

Verified results:

- Deployment: `nginx-deployment`
- Initial replicas: 2/2 ready
- Pods: Running
- Service: `nginx-service`
- Service type: NodePort
- NodePort: `30080`

The Minikube service URL was obtained from the actual command output and used to access the Nginx welcome page.

## 8. Scaling and Self-Healing Evidence

Scale the deployment from two replicas to three:

```bash
kubectl scale deployment nginx-deployment --replicas=3
kubectl rollout status deployment/nginx-deployment
kubectl get deployments
kubectl get pods
```

Observed result: the rollout completed successfully, with 3/3 replicas ready and three running Nginx pods.

Delete a running pod using its actual name:

```bash
kubectl delete pod nginx-deployment-75cc477c85-cj5f6
kubectl get deployments
kubectl get pods
```

Observed result:

- The selected pod was deleted successfully.
- Kubernetes created the replacement pod `nginx-deployment-75cc477c85-pd6s6`.
- The Deployment returned to 3/3 ready replicas, with three running pods.

The Deployment controller maintains the desired replica count. When a pod is deleted, the controller creates a replacement to restore the desired state.

The deployment manifest continues to specify two replicas; scaling to three changed the live Deployment through `kubectl scale`.

## 9. Evidence and Limitations

Keep genuine screenshots or captured command outputs showing:

- Maven test and package results.
- Git branch and commit history.
- Jenkins stages, test results, and archived JAR.
- Minikube Deployment and Service status.
- Scaling to three replicas and pod self-healing.

Known limitation: the Kubernetes deployment is an Nginx demonstration deployment, not a containerized deployment of the Java Course Registration application. The Java application is built and packaged through Maven and Jenkins.

All reported test results, Jenkins execution, and Kubernetes results are based on actual command outputs collected during this work.