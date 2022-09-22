#!/usr/bin/env bash

# Generate Certificate that can be registered for the AD Application
#
# Ref:
# * https://github.com/Azure-Samples/ms-identity-java-daemon/tree/master/msal-client-credential-certificate
# * https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-node/docs/certificate-credentials.md
# * https://learn.microsoft.com/en-us/azure/active-directory/develop/active-directory-certificate-credentials#register-your-certificate-with-microsoft-identity-platform

private_path="private/azure"

private_key_pem="${private_path}/azure-app-private-key.pem"
private_key_pkcs8="${private_path}/azure-app-private-key.pkcs8"
certificate_signing_request="${private_path}/azure-app-certificate.csr"
certificate="${private_path}/azure-app-certificate.crt"

mkdir -p ${private_path}
openssl genrsa -out ${private_key_pem} 2048
openssl pkcs8 -topk8 -inform PEM -outform DER -in ${private_key_pem} -nocrypt > ${private_key_pkcs8}
openssl req -new -key ${private_key_pem} -out ${certificate_signing_request} -subj "/C=NO/CN=ssb.no/O=statisticsnorway"
openssl x509 -req -days 365 -in ${certificate_signing_request} -signkey ${private_key_pem} -out ${certificate}
