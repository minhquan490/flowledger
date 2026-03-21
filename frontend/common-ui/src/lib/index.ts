// Core Utilities
export * from './utils.js';

// --- LAYOUT ---
export {
  Container, Stack, Inline, Grid, Section, SimpleCard, Divider,
  AspectRatio, ScrollArea, RootLayout, PageHeader, AppLayout,
  containerVariants, type ContainerSize,
  stackVariants, type StackAlign, type StackGap, type StackJustify,
  inlineVariants, type InlineAlign, type InlineGap, type InlineJustify, type InlineWrap,
  gridVariants, type GridCols, type GridGap,
  sectionVariants, type SectionSpacing
} from './components/layout/index.js';

// --- BUTTONS ---
export {
  ButtonBase, PrimaryButton, OutlinedButton, SecondaryButton, GhostButton,
  DangerButton, IconButton, OutlinedButon,
  buttonBaseVariants, type ButtonBaseProps, type ButtonBaseVariant, type ButtonBaseSize
} from './components/buttons/index.js';

// --- NAVIGATION ---
export {
  Tabs, Breadcrumb, 
  Pagination, CommandMenu, SidebarShell, CommandPalette
} from './components/navigation/index.js';

// --- FORMS ---
export {
  Input, Textarea, Select, 
  Combobox, Checkbox, RadioGroup, 
  Switch, 
  DatePicker, DateRangePicker, PhoneInput, FileUpload,
  TanStackFormField, Label, HelperText, ErrorText, RequiredMark, FormField
} from './components/forms/index.js';
export * from './components/forms/zod.js';
export * from './components/forms/types.js';

// --- DATA DISPLAY ---
export {
  Table, DataTable, ExpandableTable, List, DescriptionList, Stat, Badge, Chip,
  Avatar, Tooltip, Popover, 
  EmptyState, Skeleton
} from './components/data-display/index.js';
export { type DataTableColumn } from './components/data-display/DataTable.svelte';

// --- FEEDBACK ---
export { Progress, StatusBadge } from './components/feedback/index.js';

// --- OVERLAY ---
export { 
  Dialog, Drawer, Sheet, 
  DropdownMenu, ContextMenu, ConfirmDialog 
} from './components/overlay/index.js';

// --- CARDS ---
export {
  Card, CardHeader, CardTitle, 
  CardContent, type CardProps
} from './components/cards/index.js';

// --- BUSINESS ---
export * from './components/business/index.js';


// --- UI PRIMITIVES (Low-level access) ---
// We export the parts AND the Root components with their primary names.
// This supports standard shadcn-svelte composition patterns (e.g., Table + TableBody).

// Alert
export { Alert as ShadcnAlert, AlertTitle as ShadcnAlertTitle, AlertDescription as ShadcnAlertDescription } from './components/ui/alert/index.js';

// Aspect Ratio
export { AspectRatio as ShadcnAspectRatio } from './components/ui/aspect-ratio/index.js';

// Avatar
export { Avatar as ShadcnAvatar, AvatarImage as ShadcnAvatarImage, AvatarFallback as ShadcnAvatarFallback } from './components/ui/avatar/index.js';

// Badge
export { Badge as ShadcnBadge } from './components/ui/badge/index.js';

// Breadcrumb
export { 
  Breadcrumb as ShadcnBreadcrumb, BreadcrumbItem as ShadcnBreadcrumbItem, BreadcrumbLink as ShadcnBreadcrumbLink, BreadcrumbPage as ShadcnBreadcrumbPage, 
  BreadcrumbSeparator as ShadcnBreadcrumbSeparator, BreadcrumbEllipsis as ShadcnBreadcrumbEllipsis, BreadcrumbList as ShadcnBreadcrumbList 
} from './components/ui/breadcrumb/index.js';

// Button
export { Button as ShadcnButton, buttonVariants, type ButtonProps } from './components/ui/button/index.js';

// Calendar
export { Calendar as ShadcnCalendar } from './components/ui/calendar/index.js';

// Card
export { 
  Card as ShadcnCard, CardContent as ShadcnCardContent, CardDescription as ShadcnCardDescription, CardFooter as ShadcnCardFooter, 
  CardHeader as ShadcnCardHeader, CardTitle as ShadcnCardTitle, CardAction as ShadcnCardAction 
} from './components/ui/card/index.js';

// Checkbox
export { Checkbox as ShadcnCheckbox } from './components/ui/checkbox/index.js';

// Command
export {
  Command as ShadcnCommand, CommandDialog as ShadcnCommandDialog, CommandEmpty as ShadcnCommandEmpty, CommandGroup as ShadcnCommandGroup,
  CommandInput as ShadcnCommandInput, CommandItem as ShadcnCommandItem, CommandList as ShadcnCommandList, CommandSeparator as ShadcnCommandSeparator, CommandShortcut as ShadcnCommandShortcut
} from './components/ui/command/index.js';

// Context Menu
// Context Menu
export {
  ContextMenu as ShadcnContextMenu, ContextMenuCheckboxItem as ShadcnContextMenuCheckboxItem, ContextMenuContent as ShadcnContextMenuContent, ContextMenuGroup as ShadcnContextMenuGroup,
  ContextMenuItem as ShadcnContextMenuItem, ContextMenuLabel as ShadcnContextMenuLabel, ContextMenuPortal as ShadcnContextMenuPortal, ContextMenuRadioGroup as ShadcnContextMenuRadioGroup,
  ContextMenuRadioItem as ShadcnContextMenuRadioItem, ContextMenuSeparator as ShadcnContextMenuSeparator, ContextMenuShortcut as ShadcnContextMenuShortcut, ContextMenuSub as ShadcnContextMenuSub,
  ContextMenuSubContent as ShadcnContextMenuSubContent, ContextMenuSubTrigger as ShadcnContextMenuSubTrigger, ContextMenuTrigger as ShadcnContextMenuTrigger
} from './components/ui/context-menu/index.js';

// Dialog
export {
  Dialog as ShadcnDialog, DialogContent as ShadcnDialogContent, DialogDescription as ShadcnDialogDescription, DialogFooter as ShadcnDialogFooter,
  DialogHeader as ShadcnDialogHeader, DialogTitle as ShadcnDialogTitle, DialogTrigger as ShadcnDialogTrigger, DialogPortal as ShadcnDialogPortal, DialogOverlay as ShadcnDialogOverlay
} from './components/ui/dialog/index.js';

// Dropdown Menu
// Dropdown Menu
export {
  DropdownMenu as ShadcnDropdownMenu, DropdownMenuCheckboxItem as ShadcnDropdownMenuCheckboxItem, DropdownMenuContent as ShadcnDropdownMenuContent, DropdownMenuGroup as ShadcnDropdownMenuGroup,
  DropdownMenuItem as ShadcnDropdownMenuItem, DropdownMenuLabel as ShadcnDropdownMenuLabel, DropdownMenuPortal as ShadcnDropdownMenuPortal, DropdownMenuRadioGroup as ShadcnDropdownMenuRadioGroup,
  DropdownMenuRadioItem as ShadcnDropdownMenuRadioItem, DropdownMenuSeparator as ShadcnDropdownMenuSeparator, DropdownMenuShortcut as ShadcnDropdownMenuShortcut, DropdownMenuSub as ShadcnDropdownMenuSub,
  DropdownMenuSubContent as ShadcnDropdownMenuSubContent, DropdownMenuSubTrigger as ShadcnDropdownMenuSubTrigger, DropdownMenuTrigger as ShadcnDropdownMenuTrigger
} from './components/ui/dropdown-menu/index.js';

// Input
export { Input as ShadcnInput } from './components/ui/input/index.js';

// Label
export { Label as ShadcnLabel } from './components/ui/label/index.js';

// Pagination
// Pagination
export {
  Pagination as ShadcnPagination, PaginationContent as ShadcnPaginationContent, PaginationEllipsis as ShadcnPaginationEllipsis, PaginationItem as ShadcnPaginationItem,
  PaginationLink as ShadcnPaginationLink, PaginationNext as ShadcnPaginationNext, PaginationPrevious as ShadcnPaginationPrevious, PrevButton as ShadcnPaginationPrevButton, NextButton as ShadcnPaginationNextButton
} from './components/ui/pagination/index.js';

// Popover
export { Popover as ShadcnPopover, PopoverContent as ShadcnPopoverContent, PopoverTrigger as ShadcnPopoverTrigger } from './components/ui/popover/index.js';

// Progress
export { Progress as ShadcnProgress } from './components/ui/progress/index.js';

// Radio Group
export { RadioGroup as ShadcnRadioGroup, RadioGroupItem as ShadcnRadioGroupItem } from './components/ui/radio-group/index.js';

// Scroll Area
export { ScrollArea as ShadcnScrollArea, Scrollbar as ShadcnScrollbar } from './components/ui/scroll-area/index.js';

// Select
export {
  Select as ShadcnSelect, SelectContent as ShadcnSelectContent, SelectGroup as ShadcnSelectGroup, SelectItem as ShadcnSelectItem,
  SelectLabel as ShadcnSelectLabel, SelectSeparator as ShadcnSelectSeparator, SelectTrigger as ShadcnSelectTrigger, SelectGroupHeading as ShadcnSelectGroupHeading, SelectPortal as ShadcnSelectPortal
} from './components/ui/select/index.js';

// Separator
export { Separator as ShadcnSeparator } from './components/ui/separator/index.js';

// Sheet
export {
  Sheet as ShadcnSheet, SheetContent as ShadcnSheetContent, SheetDescription as ShadcnSheetDescription, SheetFooter as ShadcnSheetFooter,
  SheetHeader as ShadcnSheetHeader, SheetTitle as ShadcnSheetTitle, SheetTrigger as ShadcnSheetTrigger, SheetPortal as ShadcnSheetPortal, SheetOverlay as ShadcnSheetOverlay
} from './components/ui/sheet/index.js';

// Sidebar
export {
  Sidebar as ShadcnSidebar, SidebarContent as ShadcnSidebarContent, SidebarFooter as ShadcnSidebarFooter, SidebarGroup as ShadcnSidebarGroup, SidebarGroupAction as ShadcnSidebarGroupAction,
  SidebarGroupContent as ShadcnSidebarGroupContent, SidebarGroupLabel as ShadcnSidebarGroupLabel, SidebarHeader as ShadcnSidebarHeader, SidebarInput as ShadcnSidebarInput,
  SidebarInset as ShadcnSidebarInset, SidebarMenu as ShadcnSidebarMenu, SidebarMenuAction as ShadcnSidebarMenuAction, SidebarMenuBadge as ShadcnSidebarMenuBadge,
  SidebarMenuButton as ShadcnSidebarMenuButton, SidebarMenuItem as ShadcnSidebarMenuItem, SidebarMenuSkeleton as ShadcnSidebarMenuSkeleton, SidebarMenuSub as ShadcnSidebarMenuSub,
  SidebarMenuSubButton as ShadcnSidebarMenuSubButton, SidebarMenuSubItem as ShadcnSidebarMenuSubItem, SidebarProvider as ShadcnSidebarProvider, SidebarRail as ShadcnSidebarRail,
  SidebarSeparator as ShadcnSidebarSeparator, SidebarTrigger as ShadcnSidebarTrigger
} from './components/ui/sidebar/index.js';

// Skeleton
export { Skeleton as ShadcnSkeleton } from './components/ui/skeleton/index.js';

// Sonner
export { Toaster } from './components/ui/sonner/index.js';

// Switch
export { Switch as ShadcnSwitch } from './components/ui/switch/index.js';

// Table
export {
  Table as ShadcnTable, TableBody as ShadcnTableBody, TableCaption as ShadcnTableCaption, TableCell as ShadcnTableCell, TableFooter as ShadcnTableFooter,
  TableHead as ShadcnTableHead, TableHeader as ShadcnTableHeader, TableRow as ShadcnTableRow
} from './components/ui/table/index.js';

// Tabs
export {
  Tabs as ShadcnTabs, TabsContent as ShadcnTabsContent, TabsList as ShadcnTabsList, TabsTrigger as ShadcnTabsTrigger
} from './components/ui/tabs/index.js';

// Textarea
export { Textarea as ShadcnTextarea } from './components/ui/textarea/index.js';

// Tooltip
export { Tooltip as ShadcnTooltip, TooltipContent as ShadcnTooltipContent, TooltipTrigger as ShadcnTooltipTrigger, TooltipPortal as ShadcnTooltipPortal } from './components/ui/tooltip/index.js';

// --- THEME ---
export * from './theme/index.js';
