#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { Octokit } = require('@octokit/rest');

const ISSUES_DIR = path.join(__dirname, '../../.local/issues');
const CACHE_DIR = path.join(ISSUES_DIR, 'cache');
const ISSUES_CACHE_DIR = path.join(CACHE_DIR, 'issues');
const INDEX_FILE = path.join(CACHE_DIR, 'index.json');

const octokit = new Octokit({
  auth: process.env.GITHUB_TOKEN || process.env.GH_TOKEN,
});

const [owner, repo] = (process.env.GITHUB_REPOSITORY || 'gc-lover/necpgame-monorepo').split('/');

async function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function ensureDirectories() {
  [ISSUES_DIR, CACHE_DIR, ISSUES_CACHE_DIR].forEach(dir => {
    if (!fs.existsSync(dir)) {
      fs.mkdirSync(dir, { recursive: true });
    }
  });
}

async function fetchAllIssues(incremental = false) {
  const allIssues = [];
  let page = 1;
  const perPage = 100;
  
  let index = {};
  if (incremental && fs.existsSync(INDEX_FILE)) {
    index = JSON.parse(fs.readFileSync(INDEX_FILE, 'utf8'));
  }
  
  while (true) {
    try {
      console.log(`Fetching page ${page}...`);
      
      const { data: issues, headers } = await octokit.rest.issues.listForRepo({
        owner,
        repo,
        state: 'all',
        per_page: perPage,
        page,
        sort: 'updated',
        direction: 'desc',
      });
      
      if (issues.length === 0) break;
      
      for (const issue of issues) {
        if (incremental && index[issue.number]) {
          const cached = index[issue.number];
          if (new Date(issue.updated_at) <= new Date(cached.updated_at)) {
            continue;
          }
        }
        
        allIssues.push(issue);
        
        const issueFile = path.join(ISSUES_CACHE_DIR, `${issue.number}.json`);
        
        // Защита от перезаписи: проверяем pending изменения
        const pendingUpdateFile = path.join(__dirname, '../../.local/issues/pending/updates', `${issue.number}.json`);
        if (fs.existsSync(pendingUpdateFile)) {
          const pendingUpdate = JSON.parse(fs.readFileSync(pendingUpdateFile, 'utf8'));
          const pendingTime = new Date(pendingUpdate.timestamp || 0);
          const githubTime = new Date(issue.updated_at);
          
          // Если есть локальные изменения, сохраняем резервную копию
          if (pendingTime > githubTime) {
            const backupFile = path.join(__dirname, '../../.local/issues/sync/backups', `${issue.number}-${Date.now()}.json`);
            const backupDir = path.dirname(backupFile);
            if (!fs.existsSync(backupDir)) {
              fs.mkdirSync(backupDir, { recursive: true });
            }
            if (fs.existsSync(issueFile)) {
              fs.copyFileSync(issueFile, backupFile);
            }
            console.log(`  ⚠️  Issue #${issue.number} has pending changes, backup created`);
          }
        }
        
        // Сохраняем текущую версию перед обновлением
        if (fs.existsSync(issueFile)) {
          const backupFile = path.join(__dirname, '../../.local/issues/sync/backups', `${issue.number}-pre-${Date.now()}.json`);
          const backupDir = path.dirname(backupFile);
          if (!fs.existsSync(backupDir)) {
            fs.mkdirSync(backupDir, { recursive: true });
          }
          fs.copyFileSync(issueFile, backupFile);
        }
        
        fs.writeFileSync(issueFile, JSON.stringify(issue, null, 2));
        
        index[issue.number] = {
          number: issue.number,
          title: issue.title,
          state: issue.state,
          updated_at: issue.updated_at,
          cached_at: new Date().toISOString(),
        };
        
        await delay(100);
      }
      
      if (issues.length < perPage) break;
      page++;
      
      await delay(500);
      
      if (headers['x-ratelimit-remaining'] && parseInt(headers['x-ratelimit-remaining']) < 10) {
        console.log('Rate limit low, waiting...');
        await delay(60000);
      }
    } catch (error) {
      if (error.status === 403) {
        console.log('Rate limit exceeded, waiting 60 seconds...');
        await delay(60000);
        continue;
      }
      throw error;
    }
  }
  
  fs.writeFileSync(INDEX_FILE, JSON.stringify(index, null, 2));
  
  return allIssues;
}

async function fetchIssueComments(issueNumber) {
  try {
    const { data: comments } = await octokit.rest.issues.listComments({
      owner,
      repo,
      issue_number: issueNumber,
    });
    
    return comments;
  } catch (error) {
    if (error.status === 403) {
      await delay(60000);
      return fetchIssueComments(issueNumber);
    }
    return [];
  }
}

async function enrichIssuesWithComments(issues) {
  console.log('Fetching comments for issues...');
  
  for (let i = 0; i < issues.length; i++) {
    const issue = issues[i];
    const issueFile = path.join(ISSUES_CACHE_DIR, `${issue.number}.json`);
    
    if (fs.existsSync(issueFile)) {
      const cached = JSON.parse(fs.readFileSync(issueFile, 'utf8'));
      const comments = await fetchIssueComments(issue.number);
      
      cached.comments = comments;
      fs.writeFileSync(issueFile, JSON.stringify(cached, null, 2));
      
      console.log(`  [${i + 1}/${issues.length}] Issue #${issue.number} - ${comments.length} comments`);
      
      await delay(200);
    }
  }
}

async function main() {
  const incremental = process.argv.includes('--incremental');
  
  await ensureDirectories();
  
  console.log(`Syncing issues from ${owner}/${repo}...`);
  console.log(`Mode: ${incremental ? 'incremental' : 'full'}`);
  
  const issues = await fetchAllIssues(incremental);
  console.log(`\nFetched ${issues.length} issues`);
  
  if (issues.length > 0) {
    await enrichIssuesWithComments(issues);
  }
  
  console.log('\n✅ Sync completed');
  console.log(`Issues cached in: ${ISSUES_CACHE_DIR}`);
}

main().catch(console.error);

