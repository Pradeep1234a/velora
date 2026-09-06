---
name: Velora Tracker Design System
colors:
  surface: '#f8f9ff'
  surface-dim: '#cbdbf5'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d3e4fe'
  on-surface: '#0b1c30'
  on-surface-variant: '#3e4947'
  inverse-surface: '#213145'
  inverse-on-surface: '#eaf1ff'
  outline: '#6e7977'
  outline-variant: '#bdc9c6'
  surface-tint: '#006a63'
  primary: '#005c55'
  on-primary: '#ffffff'
  primary-container: '#0f766e'
  on-primary-container: '#a3faef'
  inverse-primary: '#80d5cb'
  secondary: '#006b5f'
  on-secondary: '#ffffff'
  secondary-container: '#6df5e1'
  on-secondary-container: '#006f64'
  tertiary: '#005a6a'
  on-tertiary: '#ffffff'
  tertiary-container: '#007488'
  on-tertiary-container: '#c6f2ff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#9cf2e8'
  primary-fixed-dim: '#80d5cb'
  on-primary-fixed: '#00201d'
  on-primary-fixed-variant: '#00504a'
  secondary-fixed: '#71f8e4'
  secondary-fixed-dim: '#4fdbc8'
  on-secondary-fixed: '#00201c'
  on-secondary-fixed-variant: '#005048'
  tertiary-fixed: '#acedff'
  tertiary-fixed-dim: '#4cd7f6'
  on-tertiary-fixed: '#001f26'
  on-tertiary-fixed-variant: '#004e5c'
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  display-md:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  title-lg:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Inter
    fontSize: 15px
    fontWeight: '400'
    lineHeight: 22px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-lg:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '500'
    lineHeight: 18px
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.02em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  micro: 4px
  standard: 8px
  compact: 12px
  margin: 16px
  section: 24px
  separation: 32px
  container-padding: 16px
  bottom-nav-height: 80px
  top-bar-height: 64px
---

## Brand & Style

This design system establishes a refined, high-precision aesthetic tailored for everyday personal income and expense tracking on Android. Anchored in Material 3 principles, the visual language balances fiscal seriousness with effortless approachability. 

The emotional tone evokes clarity, control, and calm reassurance. Users managing personal finances face cognitive load and potential anxiety; the interface counters this through disciplined layouts, intentional chromatic signposting, and high legibility. 

The style sits at the intersection of Modern Corporate and Tactile Precision:
- Surfaces are quiet, structured, and hierarchical, avoiding frivolous decoration.
- Financial figures command immediate authority through deliberate scale and optical balance.
- Income and expense indicators utilize distinct, non-fluorescent semantic tones—deep emerald for inflow, rich terracotta/rust for outflow—maintaining fiscal sobriety over aggressive alerting.
- Interaction cues favor crisp, physical transitions with native Android dynamic ripple and subtle state-layer shifts.

## Colors

The color architecture relies on deep teal foundations layered with aquatic accents and grounded semantic pigments.

### Core Roles
- **Primary (`#0F766E`)**: Deep teal providing an anchor for top app bars, primary action buttons, focused controls, and brand marks.
- **Primary Accent / Secondary (`#14B8A6`)**: Bright teal for interactive highlights, active indicator pills, and progress tracks.
- **Tertiary Accent (`#06B6D4`)**: Cyan for auxiliary data visualization points, category tags, and secondary graphical cues.
- **Supporting Blue (`#2563EB`)**: Utilized strictly for system feedback, linked accounts, informational badges, and neutral transfers.

### Semantic Tones
- **Income (`#16803C`)**: Clean, natural emerald green denoting positive cash flow, credits, and budget surpluses.
- **Expense (`#C2410C`)**: Deep, warm terracotta rust designating debits, expenditure categories, and threshold warnings without invoking panic.

### Surface & Canvas Architecture
- **Light Theme**:
  - Background Canvas: `#F8FAFC` (Clean slate off-white)
  - Surface Default: `#FFFFFF`
  - Outline / Divider: `#E2E8F0`
- **Dark Theme**:
  - Background Canvas: `#0B1110` (Deep obsidian teal)
  - Surface Default: `#131B19` (Elevated charcoal teal)
  - Outline / Divider: `#1E2927`

## Typography

The typographic hierarchy uses Inter (with Roboto as platform native parity) across all Android endpoints to secure neutral glyph clarity and tabular number alignment.

### Financial Number Rendering
- All monetary balances and transaction quantities (`display-lg`, `display-md`, and numeric variants of `body-md`) must enforce `font-variant-numeric: tabular-nums` or `tnum`.
- Currency symbols precede amounts with a slight visual step-down in weight or opacity to keep focus on whole and fractional values.

### Hierarchy Applications
- **Display (`32px–40px`)**: Reserved strictly for top-level net worth, aggregate monthly cash flows, and hero analytics balances.
- **Headline (`24px–28px`)**: Major screen anchors, onboarding welcomes, and full-screen bottom-sheet headings.
- **Title (`16px–20px`)**: Card headers, transaction group timestamps (e.g., "Today", "Yesterday"), and modal viewports.
- **Body (`14px–15px`)**: Core ledger entry names, merchant descriptions, category subtitles, and field help text.
- **Label (`11px–13px`)**: Segmented buttons, navigation bar items, filter chips, and transaction timestamp captions.

## Layout & Spacing

The layout conforms to a strict 8dp spatial rhythm optimized for Android touch points and dynamic window sizes.

### Spatial Increments
- **4dp (`micro`)**: Internal component padding (e.g., icon-to-label gaps, tag paddings, badge insets).
- **8dp (`standard`)**: Compact list item spacing, adjacent chip gutters, and nested card paddings.
- **12dp (`compact`)**: Field vertical stack padding and sub-card divisions.
- **16dp (`margin`)**: Standard lateral screen padding, master view gutters, and card container insets.
- **24dp (`section`)**: Vertical distances between semantic blocks (e.g., balance card to transaction list).
- **32dp (`separation`)**: Distance separating distinct interactive workflows or major summary segments.

### Viewport Adaptation
- **Mobile (Compact: < 600dp)**: Single-column flow with persistent 16dp outer screen margin. Floating Action Button anchored to the bottom right with a 16dp offset above the 80dp navigation bar.
- **Tablet (Medium / Expanded: 600dp+)**: Screen margins expand to 24dp. The dashboard transitions from a stacked list to a 2-column masonry grid (metrics on left, activity stream on right). Navigation transitions from bottom bar to a vertical navigation rail.

## Elevation & Depth

This design system minimizes dramatic drop shadows in favor of tonal surface layering paired with low-contrast structural outlines, matching contemporary Material 3 specifications.

### Surface Tonal Hierarchy
- **Canvas Base (Level 0)**: Un-elevated backdrop (`#F8FAFC` light / `#0B1110` dark). Holds non-interactive structural space.
- **Surface Level 1**: Standard content surfaces such as `VeloraTransactionCard` and metric groupings (`#FFFFFF` light / `#131B19` dark).
- **Surface Level 2**: Sticky headers, docked search inputs, and elevated interactive cards. Receives a subtle ambient tint of Primary (`#0F766E`) at 3% opacity in light mode and 6% opacity in dark mode.
- **Surface Level 3**: Modal overlays, dialogs, and dynamic bottom sheets.

### Outlines & Borders
- Rather than heavy elevation, containers utilize a continuous 1px structural stroke:
  - Light mode: `#E2E8F0`
  - Dark mode: `#1E2927`
- Floating elements (FAB, active modal sheets) leverage an ambient-only tinted shadow:
  - `box-shadow: 0px 8px 24px -4px rgba(15, 118, 110, 0.12)`

## Shapes

Shapes adhere strictly to the Material 3 shape scale, creating visual rhythm between wide operational areas and tactile touch targets.

- **`rounded-2xl` (16dp)**: Primary card containers, metric summary blocks, and transaction cards.
- **`rounded-xl` (12dp)**: Input fields, filter chips, secondary nested panels, and dialog action blocks.
- **`rounded-full` (9999dp)**: Floating action buttons (FAB), status tags, amount pill indicators, and tab selectors.
- **`rounded-t-3xl` (28dp)**: Bottom sheets, account selector drawers, and full-bleed action panels.

## Components

### VeloraTopAppBar
- Fixed height: 64dp.
- Integrates current screen title (`title-lg`), date/range filter dropdown, and profile or notification iconography.
- Zero drop shadow; uses 1px bottom border matching the outline token when scrolled under.

### VeloraNavigationBar
- Fixed height: 80dp.
- Exactly 4 destinations: Dashboard, Transactions, Analytics, Categories.
- Active states feature a horizontal pill background (`rounded-full`, Primary color at 12% tint in light, 20% in dark) wrapping a filled icon with a `label-md` caption underneath.

### VeloraTransactionCard
- Surface Level 1 with 16dp border radius (`rounded-2xl`) and 1px border outline.
- Layout:
  - Left: 40dp circular icon container colored by category theme.
  - Center: Merchant or title in `body-lg` (weight 600) with timestamp/category subtitle in `label-md`.
  - Right: `VeloraAmountText` aligned right, stacked with running payment method or state.
- In-list vertical item spacing: 8dp.

### VeloraMetric
- Analytical container for Net Worth, Inflow, and Outflow.
- Top section: Micro-label indicating period (`label-sm`).
- Center: `VeloraAmountText` at `display-md` or `headline-lg`.
- Bottom: Delta comparison badge (e.g., "+4.2% vs last month") rendered inside an inset pill.

### VeloraAmountText
- Format: `[+/-][Symbol][Integer].[Fraction]`
- Positive amounts strictly enforce the Income token (`#16803C`).
- Negative amounts strictly enforce the Expense token (`#C2410C`).
- Neutral balances default to high-contrast surface foregrounds.
- Monospace tabular figures prevent jitter during live updates.

### VeloraFAB
- Shape: `rounded-full` (or M3 standard large-surface rounded container).
- Color: Primary (`#0F766E`) with high-contrast icon foreground.
- Action: Global fast-action trigger to record a new transaction with single-tap haptic confirmation.

### Buttons & Inputs
- **Filled Button**: 48dp target height, `rounded-xl`, Primary background, white label (`label-lg`).
- **Input Fields**: 56dp container, `rounded-xl`, 1px outline resting state, expanding to 2px Primary stroke on focus. Embedded numeric inputs display clear currency adornments.