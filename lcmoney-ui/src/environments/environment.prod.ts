export const environment = {
  production: true,
  apiURL: 'http://localhost:8080',
  tokenAllowedDomains: [  /localhost:8080/ ],
  tokenDisallowedRoutes: [/\/oauth\/token/],
};
