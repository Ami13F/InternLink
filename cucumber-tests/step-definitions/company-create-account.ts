import { Given, Then, When } from 'cucumber';

Given('the company username {string} and password {string}', async function (string, string2) {
  // Write code here that turns the phrase above into concrete actions
  return `pending ${string} ${string2}`;
});

When('I make a request to create a company account', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});

Then("I receive back a token representing the new company's credentials", async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});
