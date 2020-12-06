import { Given, When, Then } from 'cucumber';

Given('that I am authenticated with email: {string} and password: {string}', async function (
  string,
  string2,
) {
  // Write code here that turns the phrase above into concrete actions
  return `pending ${string} ${string2}`;
});

Given('an internship to which applications are closed', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});

When('I make a request to create an application for a closed internship', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});

Then('I receive a message informing me that I am unable to apply', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});
