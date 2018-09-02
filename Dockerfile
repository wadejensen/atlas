FROM node:8-alpine

RUN mkdir -p /etc/service/atlas

COPY backend-js  /etc/service/atlas/backend-js
COPY frontend-js /etc/service/atlas/frontend-js

CMD ["node","/etc/service/atlas/backend-js/app.js"]
