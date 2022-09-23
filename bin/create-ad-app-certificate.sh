#!/usr/bin/env bash

# Generate Certificate that can be registered for the AD Application
#
# Ref:
# * https://github.com/Azure-Samples/ms-identity-java-daemon/tree/master/msal-client-credential-certificate
# * https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-node/docs/certificate-credentials.md
# * https://learn.microsoft.com/en-us/azure/active-directory/develop/active-directory-certificate-credentials#register-your-certificate-with-microsoft-identity-platform

private_path="private/ad"
app_name="ad-app"
certificate_password="replaceme"

privatekey_pem="${private_path}/${app_name}-private-key.pem"
privatekey_pkcs8="${private_path}/${app_name}-private-key.pkcs8"
certificate_signing_request="${private_path}/${app_name}-certificate.csr"
certificate_crt="${private_path}/${app_name}-certificate.crt"
certificate_pfx="${private_path}/${app_name}-certificate.pfx"

mkdir -p ${private_path}
openssl genrsa -out ${privatekey_pem} 2048
openssl pkcs8 -topk8 -inform PEM -outform DER -in ${privatekey_pem} -nocrypt > ${privatekey_pkcs8}
openssl req -new -key ${privatekey_pem} -out ${certificate_signing_request} -subj "/C=NO/CN=ssb.no/O=statisticsnorway"
openssl x509 -req -days 365 -in ${certificate_signing_request} -signkey ${privatekey_pem} -out ${certificate_crt}
openssl pkcs12 -export -out ${certificate_pfx} -inkey ${privatekey_pem} -in ${certificate_crt} -password pass:${certificate_password}

echo "Go to portal.azure.com and manage the Dapla Team API aad application. Register this certificate:"
echo "${certificate_crt}"
echo
echo "You can then use this certificate to interact with the MS Graph API:"
echo "${certificate_pfx}"
echo "Certificate password: ${certificate_password}"
