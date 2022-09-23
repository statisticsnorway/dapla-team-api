#!/usr/bin/env bash

env="staging"
platform_dev_path="$HOME/dev/code/ssb/platform-dev"
secret_name="dapla-team-api-ad-app"
ad_app_certificate_path="private/ad/ad-app-certificate.pfx"
ad_app_certificate_password="replaceme"
target_file="${platform_dev_path}/flux/${env}-bip-app/dapla/dapla-team-api/${secret_name}-sealedsecret.yaml"

kubectl create secret generic ${secret_name} \
-n dapla \
--dry-run=client \
--from-file=${ad_app_certificate_path} \
--from-literal=ad-app-certificate-password="${ad_app_certificate_password}" \
-o yaml | kubeseal --format=yaml --cert ${platform_dev_path}/certs/${env}-bip-app.crt > ${target_file}

echo "Sealed secret file: ${target_file}"