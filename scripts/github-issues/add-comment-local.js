#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

const PENDING_COMMENTS_DIR = path.join(__dirname, '../../.local/issues/pending/comments');

function ensureDirectories() {
  if (!fs.existsSync(PENDING_COMMENTS_DIR)) {
    fs.mkdirSync(PENDING_COMMENTS_DIR, { recursive: true });
  }
}

function addCommentLocal(issueNumber, body) {
  ensureDirectories();
  
  const commentFile = path.join(PENDING_COMMENTS_DIR, `${issueNumber}-${Date.now()}.json`);
  
  const comment = {
    issue_number: issueNumber,
    body,
    created_at: new Date().toISOString(),
  };
  
  fs.writeFileSync(commentFile, JSON.stringify(comment, null, 2));
  
  console.log(`OK Comment added locally for issue #${issueNumber}`);
  console.log(`   Pending sync: ${commentFile}`);
}

function main() {
  const issueNumber = parseInt(process.argv[2]);
  const body = process.argv[3];
  
  if (!issueNumber || !body) {
    console.error('Usage: add-comment-local.js <issue-number> "<comment body>"');
    process.exit(1);
  }
  
  addCommentLocal(issueNumber, body);
}

main();

