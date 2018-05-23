
Feature: Checking various number plates
  As a person
  I want to check up a reg plate
  So that I can see information on the reg plate

Scenario: Browse the items available on the website
  Given the correct web address
  When I submit a reg number
  Then I can view information on that reg plate

