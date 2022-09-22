#!/usr/bin/env bash

secret_name="dapla-team-api-github-app"
platform_dev_path="$HOME/dev/code/ssb/platform-dev"
github_app_private_key_path="private/github/github-app-private-key.der"
github_app_client_secret="replaceme"
env="staging"
target_file="${platform_dev_path}/flux/${env}-bip-app/dapla/dapla-team-api/${secret_name}-sealedsecret.yaml"

kubectl create secret generic ${secret_name} \
-n dapla \
--dry-run=client \
--from-file=${github_app_private_key_path} \
--from-literal=github-app-client-secret="${github_app_client_secret}" \
-o yaml | kubeseal --format=yaml --cert ${platform_dev_path}/certs/${env}-bip-app.crt > ${target_file}

echo "Sealed secret file: ${target_file}"