// The docker-machine ip
// const API_IP: string = process.env.IP || "192.168.99.100";

const API_IP: string = process.env.IP || "localhost";

// or ubuntu vm ip running docker exposing the gateway and eureka
// const API_IP: string = process.env.IP || "192.168.48.129";
const API_PORT: string = process.env.PORT || "8000";

// points to the spring cloud API gateway (Zuul)
export const API_URL: string = `http://${API_IP}:${API_PORT}/api`;