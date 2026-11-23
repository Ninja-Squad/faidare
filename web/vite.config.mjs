import autoprefixer from 'autoprefixer';
import { defineConfig } from 'vite';

export default defineConfig(({ mode }) => {
    const production = mode === 'production';
    // Keep stable JS and CSS filenames in development; Gradle picks up the hashed names for the JAR.
    const hash = production ? '.[hash]' : '';

    return {
        // Keep asset URLs relative so Spring can serve them under any context path.
        base: './',
        css: { postcss: { plugins: [autoprefixer()] } },
        build: {
            // Spring serves these files in development, and Gradle copies them into the JAR.
            outDir: 'build/dist/resources',
            target: 'es2015',
            minify: production,
            // Extract the Sass imported by index.ts into one stylesheet, without a separate script entry.
            cssCodeSplit: false,
            cssMinify: production,
            // Include inline JS source maps only in development builds.
            sourcemap: production ? false : 'inline',
            assetsInlineLimit: 0,
            license: production ? { fileName: 'licenses.md' } : false,
            rolldownOptions: {
                // The app is small enough to share one JS bundle across all Thymeleaf pages.
                input: 'src/index.ts',
                output: {
                    // Thymeleaf invokes window.faidare in an inline script immediately after this bundle.
                    format: 'iife',
                    entryFileNames: `script${hash}.js`,
                    assetFileNames: asset =>
                        asset.names.some(name => name.endsWith('.css')) ? `style${hash}.css` : '[name].[hash][extname]'
                }
            }
        }
    };
});
