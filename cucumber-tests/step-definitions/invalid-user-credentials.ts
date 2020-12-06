import { Given, When, Then } from 'cucumber';

Given('a username {string} and password {string}', async function (string, string2) {
  // Write code here that turns the phrase above into concrete actions
  return `pending ${string} ${string2}`;
});

When('I make a request to login into the system with invalid credentials', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});

Then('I receive a message informing me that the credentials are invalid', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});
