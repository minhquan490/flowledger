<script lang="ts">
  import './layout.css';
  import favicon from '$lib/assets/favicon.svg';
  import { AppLayout, Toast } from '@medisphere/common-ui';
  import { LayoutDashboard, Users, ShieldAlert, FileText, Settings } from '@lucide/svelte';
  import { page } from '$app/state';
  import { QueryClient, QueryClientProvider } from '@tanstack/svelte-query';

  const queryClient = new QueryClient();

  let { children } = $props();

  let sidebarOpen = $state(true);

  let paths = $derived(page.url.pathname.split('/').filter(Boolean));

  const PAGE_TITLES: Record<string, { title: string; subtitle?: string }> = {
    '/': { title: 'Dashboard' },
    '/users': { title: 'Users' },
    '/roles': { title: 'Roles & Permissions' },
    '/audit': { title: 'Audit Logs' },
    '/settings': { title: 'Settings' },
    '/rbac': { title: 'Permission Management' }
  };

  let breadcrumbs = $derived.by(() => {
    if (paths.length === 0) {
      return [{ label: PAGE_TITLES['/']?.title ?? 'Home', href: '/' }];
    }

    const crumbs = [];
    let currentPath = '';

    for (const p of paths) {
      currentPath += `/${p}`;

      const title = PAGE_TITLES[currentPath]?.title;
      const fallback = p.charAt(0).toUpperCase() + p.slice(1);

      crumbs.push({
        label: title ?? fallback,
        href: currentPath
      });
    }

    return [{ label: PAGE_TITLES['/']?.title ?? 'Home', href: '/' }, ...crumbs];
  });

  let currentPageInfo = $derived(PAGE_TITLES[page.url.pathname] || { title: 'Not Found' });
  let pageTitle = $derived(currentPageInfo.title);
  let pageSubtitle = $derived(currentPageInfo.subtitle);
</script>

<svelte:head>
  <title>{pageTitle}{pageSubtitle ? ` - ${pageSubtitle}` : ''} | Admin UI</title>
  <link rel="icon" href={favicon} />
</svelte:head>

<QueryClientProvider client={queryClient}>
  <AppLayout
    appTitle="Admin UI"
    {pageTitle}
    {pageSubtitle}
    {breadcrumbs}
    bind:sidebarOpen
    sidebarItems={[
      {
        id: 'dashboard',
        label: 'Dashboard',
        href: '/',
        icon: LayoutDashboard,
        active: page.url.pathname === '/'
      },
      {
        id: 'users',
        label: 'Users',
        href: '/users',
        icon: Users,
        active: page.url.pathname.startsWith('/users')
      },
      {
        id: 'roles',
        label: 'Roles & Permissions',
        href: '/rbac',
        icon: ShieldAlert,
        active: page.url.pathname.startsWith('/rbac')
      },
      {
        id: 'audit',
        label: 'Audit Logs',
        href: '/audit',
        icon: FileText,
        active: page.url.pathname.startsWith('/audit')
      },
      {
        id: 'settings',
        label: 'Settings',
        href: '/settings',
        icon: Settings,
        active: page.url.pathname.startsWith('/settings')
      }
    ]}
  >
    {@render children()}
  </AppLayout>
  <Toast showDemoButton={false} />
</QueryClientProvider>
