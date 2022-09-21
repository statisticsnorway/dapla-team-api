#!/usr/bin/env bash

# Generate Certificate that can be registered for the AD Application
#
# Ref:
# * https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-node/docs/certificate-credentials.md
# * https://learn.microsoft.com/en-us/azure/active-directory/develop/active-directory-certificate-credentials#register-your-certificate-with-microsoft-identity-platform

openssl req -x509 -newkey rsa:2048 -keyout azure-app-private-key.key -out azure-app-certificate.crt -subj "/CN=ssb.no"
