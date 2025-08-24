external:
  product:
    service:
      url: ${EXTERNAL_PRODUCT_SERVICE_URL}
      timeout: 5000
      retry:
        maxAttempts: 3
        delay: 1000
