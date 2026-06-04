import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 10,
  duration: '30s',
};

const BASE_URL = 'http://localhost:8080';

export function setup() {
  const loginRes = http.post(`${BASE_URL}/api/login`, JSON.stringify({
    email: 'perf@test.com',
    password: 'Password123'
  }), {
    headers: { 'Content-Type': 'application/json' }
  });

  return { token: loginRes.body };
}

export default function (data) {
  const res = http.get(`${BASE_URL}/api/files`, {
    headers: {
      'Authorization': `Bearer ${data.token}`
    }
  });

  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 500ms': (r) => r.timings.duration < 500,
  });

  sleep(1);
}