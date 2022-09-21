.PHONY: default
default: | help

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: build
build: build-mvn build-docker ## Build all and create docker image

.PHONY: build-mvn
build-mvn: ## Build project and install to you local maven repo
	./mvnw clean install

.PHONY: build-docker
build-docker: ## Build the docker image
	docker build -t dapla-team-api:dev -f Dockerfile .

.PHONY: release-dryrun
release-dryrun: ## Simulate a release in order to detect any issues
	./mvnw release:prepare release:perform -Darguments="-Dmaven.deploy.skip=true" -DdryRun=true

.PHONY: release
release: ## Release a new version. Update POMs and tag the new version in git
	./mvnw release:prepare release:perform -Darguments="-Dmaven.deploy.skip=true -Dmaven.javadoc.skip=true"

.PHONY: run-local
run-local: ## Run the app locally (without docker)
	java -jar  target/dapla-team-api-*.jar

.PHONY: create-github-app-sealedsecret
create-github-app-sealedsecret: ## Create the GitHub app sealed secret
	kubectl create secret generic dapla-team-api-github-app \
  	-n dapla \
	--dry-run=client \
	--from-file=${github_app_private_key_location} \
	--from-literal=ssb-dapla-team-api-client-secret=${github_app_client_secret} \
	-o yaml | kubeseal --format=yaml --cert ${platform_dev_location}/certs/${env}-bip-app.crt > dapla-team-api-github-app-sealedsecret.yaml
