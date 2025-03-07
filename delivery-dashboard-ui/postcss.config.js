// postcss.config.js
const purgecss = require('@fullhuman/postcss-purgecss');

module.exports = (ctx) => {
  return {
    plugins: [
      purgecss({
        content: [
          // Paths to your Angular templates, TypeScript, and other HTML files
          './src/**/*.html',
          './src/**/*.ts',
          './src/**/*.scss'
        ],
        defaultExtractor: (content) => content.match(/[\w-/:]+(?<!:)/g) || [],
        safelist: {
          // Add classes that are dynamically added (e.g., via JavaScript)
          standard: [/^mat-/, /^cdk-/, /^ng-/, 'dynamic-class'],
          greedy: [/^app-/]
        }
      })
    ]
  };
};
