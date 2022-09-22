#!/usr/bin/env bash

env="staging"
platform_dev_path="$HOME/dev/code/ssb/platform-dev"
secret_name="dapla-team-api-azure-app"
azure_app_private_key_path="private/azure/azure-app-private-key.key"
azure_app_certificate_path="private/azure/azure-app-certificate.crt"
target_file="${platform_dev_path}/flux/${env}-bip-app/dapla/dapla-team-api/${secret_name}-sealedsecret.yaml"

kubectl create secret generic ${secret_name} \
-n dapla \
--dry-run=client \
--from-file=${azure_app_private_key_path} \
--from-file=${azure_app_certificate_path} \
-o yaml | kubeseal --format=yaml --cert ${platform_dev_path}/certs/${env}-bip-app.crt > ${target_file}

echo "Sealed secret file: ${target_file}"