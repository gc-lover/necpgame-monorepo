#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { Octokit } = require('@octokit/rest');

const ISSUES_DIR = path.join(__dirname, '../../.local/issues');
const PENDING_DIR = path.join(ISSUES_DIR, 'pending');
const UPDATES_DIR = path.join(PENDING_DIR, 'updates');
const COMMENTS_DIR = path.join(PENDING_DIR, 'comments');
const LABELS_DIR = path.join(PENDING_DIR, 'labels');

const octokit = new Octokit({
  auth: process.env.GITHUB_TOKEN || process.env.GH_TOKEN,
});

const [owner, repo] = (process.env.GITHUB_REPOSITORY || 'gc-lover/necpgame-monorepo').split('/');

async function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function ensureDirectories() {
  [ISSUES_DIR, PENDING_DIR, UPDATES_DIR, COMMENTS_DIR, LABELS_DIR].forEach(dir => {
    if (!fs.existsSync(dir)) {
      fs.mkdirSync(dir, { recursive: true });
    }
  });
}

async function syncUpdates() {
  if (!fs.existsSync(UPDATES_DIR)) return;
  
  const updateFiles = fs.readdirSync(UPDATES_DIR)
    .filter(f => f.endsWith('.json'))
    .map(f => ({
      file: path.join(UPDATES_DIR, f),
      number: parseInt(f.replace('.json', '')),
    }))
    .sort((a, b) => a.number - b.number);
  
  console.log(`Found ${updateFiles.length} pending updates`);
  
  const batchSize = 5;
  for (let i = 0; i < updateFiles.length; i += batchSize) {
    const batch = updateFiles.slice(i, i + batchSize);
    
    for (const { file, number } of batch) {
      try {
        const update = JSON.parse(fs.readFileSync(file, 'utf8'));
        
        const updateData = {};
        if (update.title) updateData.title = update.title;
        if (update.body) updateData.body = update.body;
        if (update.state) updateData.state = update.state;
        if (update.labels) updateData.labels = update.labels;
        
        await octokit.rest.issues.update({
          owner,
          repo,
          issue_number: number,
          ...updateData,
        });
        
        console.log(`  OK Updated issue #${number}`);
        fs.unlinkSync(file);
        
        await delay(500);
      } catch (error) {
        if (error.status === 403) {
          console.log('  WARNING  Rate limit, waiting...');
          await delay(60000);
          continue;
        }
        console.error(`  ❌ Error updating issue #${number}:`, error.message);
      }
    }
    
    if (i + batchSize < updateFiles.length) {
      await delay(1000);
    }
  }
}

async function syncComments() {
  if (!fs.existsSync(COMMENTS_DIR)) return;
  
  const commentFiles = fs.readdirSync(COMMENTS_DIR)
    .filter(f => f.endsWith('.json'))
    .map(f => ({
      file: path.join(COMMENTS_DIR, f),
      data: JSON.parse(fs.readFileSync(path.join(COMMENTS_DIR, f), 'utf8')),
    }));
  
  console.log(`Found ${commentFiles.length} pending comments`);
  
  const batchSize = 5;
  for (let i = 0; i < commentFiles.length; i += batchSize) {
    const batch = commentFiles.slice(i, i + batchSize);
    
    for (const { file, data } of batch) {
      try {
        await octokit.rest.issues.createComment({
          owner,
          repo,
          issue_number: data.issue_number,
          body: data.body,
        });
        
        console.log(`  OK Added comment to issue #${data.issue_number}`);
        fs.unlinkSync(file);
        
        await delay(500);
      } catch (error) {
        if (error.status === 403) {
          console.log('  WARNING  Rate limit, waiting...');
          await delay(60000);
          continue;
        }
        console.error(`  ❌ Error adding comment:`, error.message);
      }
    }
    
    if (i + batchSize < commentFiles.length) {
      await delay(1000);
    }
  }
}

async function syncLabels() {
  if (!fs.existsSync(LABELS_DIR)) return;
  
  const labelFiles = fs.readdirSync(LABELS_DIR)
    .filter(f => f.endsWith('.json'))
    .map(f => ({
      file: path.join(LABELS_DIR, f),
      data: JSON.parse(fs.readFileSync(path.join(LABELS_DIR, f), 'utf8')),
    }));
  
  console.log(`Found ${labelFiles.length} pending label changes`);
  
  const batchSize = 5;
  for (let i = 0; i < labelFiles.length; i += batchSize) {
    const batch = labelFiles.slice(i, i + batchSize);
    
    for (const { file, data } of batch) {
      try {
        await octokit.rest.issues.update({
          owner,
          repo,
          issue_number: data.issue_number,
          labels: data.labels,
        });
        
        console.log(`  OK Updated labels for issue #${data.issue_number}`);
        fs.unlinkSync(file);
        
        await delay(500);
      } catch (error) {
        if (error.status === 403) {
          console.log('  WARNING  Rate limit, waiting...');
          await delay(60000);
          continue;
        }
        console.error(`  ❌ Error updating labels:`, error.message);
      }
    }
    
    if (i + batchSize < labelFiles.length) {
      await delay(1000);
    }
  }
}

async function main() {
  await ensureDirectories();
  
  console.log(`Syncing pending changes to ${owner}/${repo}...\n`);
  
  await syncUpdates();
  await syncComments();
  await syncLabels();
  
  console.log('\nOK Sync to GitHub completed');
}

main().catch(console.error);

