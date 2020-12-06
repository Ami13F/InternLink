import { Given, When, Then } from 'cucumber';

Given(
  'that I am authenticated as a student with email {string} and password {string}',
  async function (string, string2) {
    // Write code here that turns the phrase above into concrete actions
    return `pending ${string} ${string2}`;
  },
);
When('I make a request to receive all the internships', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});
Then('I receive a list containing all the internships', async function () {
  // Write code here that turns the phrase above into concrete actions
  return 'pending';
});
