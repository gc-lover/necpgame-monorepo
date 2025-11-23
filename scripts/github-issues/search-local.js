#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

const INDEX_FILE = path.join(__dirname, '../../.local/issues/cache/index.json');
const ISSUES_CACHE_DIR = path.join(__dirname, '../../.local/issues/cache/issues');

function searchIssues(query) {
  if (!fs.existsSync(INDEX_FILE)) {
    console.error('No local issues cache found. Run sync-from-github.js first.');
    process.exit(1);
  }
  
  const index = JSON.parse(fs.readFileSync(INDEX_FILE, 'utf8'));
  const queryLower = query.toLowerCase();
  
  const matchingIssues = Object.values(index).filter(issue => {
    const title = (issue.title || '').toLowerCase();
    const number = issue.number.toString();
    
    return title.includes(queryLower) || number.includes(queryLower);
  });
  
  if (!fs.existsSync(ISSUES_CACHE_DIR)) {
    return matchingIssues;
  }
  
  const detailedIssues = matchingIssues.map(issue => {
    const issueFile = path.join(ISSUES_CACHE_DIR, `${issue.number}.json`);
    if (fs.existsSync(issueFile)) {
      return JSON.parse(fs.readFileSync(issueFile, 'utf8'));
    }
    return issue;
  });
  
  return detailedIssues;
}

function filterByLabels(issues, labelQuery) {
  if (!labelQuery) return issues;
  
  const labels = labelQuery.split(',').map(l => l.trim().toLowerCase());
  
  return issues.filter(issue => {
    const issueLabels = (issue.labels || []).map(l => 
      typeof l === 'string' ? l.toLowerCase() : l.name.toLowerCase()
    );
    
    return labels.every(label => 
      issueLabels.some(il => il.includes(label))
    );
  });
}

function main() {
  const query = process.argv[2] || '';
  const labelFilter = process.argv.find(arg => arg.startsWith('--labels='))?.split('=')[1];
  
  let issues = searchIssues(query);
  
  if (labelFilter) {
    issues = filterByLabels(issues, labelFilter);
  }
  
  console.log(`Found ${issues.length} issues:\n`);
  
  issues.forEach(issue => {
    const labels = (issue.labels || []).map(l => 
      typeof l === 'string' ? l : l.name
    ).join(', ');
    
    console.log(`#${issue.number}: ${issue.title}`);
    console.log(`  State: ${issue.state}`);
    console.log(`  Labels: ${labels || 'none'}`);
    console.log(`  Updated: ${issue.updated_at}`);
    console.log('');
  });
}

main();

