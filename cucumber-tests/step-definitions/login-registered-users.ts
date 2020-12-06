import { Given, Then, When } from 'cucumber';

Given('a valid username {string} and password {string}', async function (string, string2) {
  // Write code here that turns the phrase above into concrete actions
  return `pending ${string} ${string2}`;
});

When('I make a request to login into the system as a registered user', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});

Then('I receive a token representing my user session', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});
