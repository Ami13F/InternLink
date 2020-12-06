import { Given, Then, When } from 'cucumber';

Given('a candidate application to one of my internships', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});
When('I make a request to update the state of the application to {string}', async function (
  string,
) {
  // Write code here that turns the phrase above into concrete actions
  return `pending ${string}`;
});
Then('I receive back the updated application', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});
