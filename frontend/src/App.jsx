import HeroSection from './components/HeroSection';
import NextSteps from './components/NextSteps';
import './App.css';

export default function App() {
  return (
    <>
      <HeroSection />
      <div className="ticks"></div>
      <NextSteps />
      <div className="ticks"></div>
      <section id="spacer"></section>
    </>
  );
}
