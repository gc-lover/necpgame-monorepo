#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

const ISSUES_CACHE_DIR = path.join(__dirname, '../../.local/issues/cache/issues');
const PENDING_UPDATES_DIR = path.join(__dirname, '../../.local/issues/pending/updates');

function ensureDirectories() {
  [PENDING_UPDATES_DIR].forEach(dir => {
    if (!fs.existsSync(dir)) {
      fs.mkdirSync(dir, { recursive: true });
    }
  });
}

function updateLocalIssue(issueNumber, options) {
  ensureDirectories();
  
  const issueFile = path.join(ISSUES_CACHE_DIR, `${issueNumber}.json`);
  const pendingFile = path.join(PENDING_UPDATES_DIR, `${issueNumber}.json`);
  
  let issue = {};
  if (fs.existsSync(issueFile)) {
    issue = JSON.parse(fs.readFileSync(issueFile, 'utf8'));
  }
  
  const update = {
    number: issueNumber,
    ...options,
  };
  
  if (options.labels) {
    update.labels = Array.isArray(options.labels) 
      ? options.labels 
      : options.labels.split(',').map(l => l.trim());
  }
  
  fs.writeFileSync(pendingFile, JSON.stringify(update, null, 2));
  
  if (fs.existsSync(issueFile)) {
    Object.assign(issue, update);
    fs.writeFileSync(issueFile, JSON.stringify(issue, null, 2));
  }
  
  console.log(`✅ Issue #${issueNumber} updated locally`);
  console.log(`   Pending sync: ${pendingFile}`);
}

function main() {
  const issueNumber = parseInt(process.argv[2]);
  
  if (!issueNumber) {
    console.error('Usage: update-local.js <issue-number> [--labels="label1,label2"] [--title="Title"] [--state="open|closed"]');
    process.exit(1);
  }
  
  const options = {};
  
  process.argv.slice(3).forEach(arg => {
    if (arg.startsWith('--labels=')) {
      options.labels = arg.split('=')[1];
    } else if (arg.startsWith('--title=')) {
      options.title = arg.split('=')[1];
    } else if (arg.startsWith('--state=')) {
      options.state = arg.split('=')[1];
    }
  });
  
  updateLocalIssue(issueNumber, options);
}

main();

